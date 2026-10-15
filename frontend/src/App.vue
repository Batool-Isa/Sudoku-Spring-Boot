<script setup>
import { ref, onMounted } from "vue";

const score = ref(0);
const name = ref("");
const board = ref([]);
const mistake = ref(0);
const time = ref("00:00");
const puzzleName = ref("");
const isGameOver = ref(false);
const isWon = ref(false);
let startTime = Date.now();
let timerId = null;

function startTimer() {
  startTime = Date.now();
  clearInterval(timerId);
  timerId = setInterval(() => {
    if (isGameOver.value || isWon.value) return;
    const totalSeconds = Math.floor((Date.now() - startTime) / 1000);
    const m = String(Math.floor(totalSeconds / 60)).padStart(2, "0");
    const s = String(totalSeconds % 60).padStart(2, "0");
    time.value = `${m}:${s}`;
  }, 1000);
}
async function getBoard() {
  const response = await fetch("http://localhost:8080/api/sudoku/loadBoard");
  if (!response.ok) {
    console.error("Failed to fetch board:", response.statusText);
    return;
  }

  const boardData = await response.json();
  puzzleName.value = boardData.puzzleName;
  board.value = boardData.grid.map((row) =>
    row.map((cell) => ({
      value: cell.value,
      fixed: cell.value !== 0,
    })),
  );
}
async function reset() {
  isGameOver.value = false;
  isWon.value = false;
  score.value = 0;
  mistake.value = 0;
  time.value = "00:00";
  startTime = Date.now();
  await getBoard();
}
async function isUserInputValid() {
  const response = await fetch("http://localhost:8080/api/sudoku/loadBoard");
  if (!response.ok) {
    const errorData = await response.json();
    console.error(errorData.message);

    return;
  }
  const boardData = await response.json();
  board.value = boardData.map((row) =>
    row.map((cell) => ({
      value: cell.value,
      fixed: cell.value !== 0,
    })),
  );
}
function updateScore(newScore) {
  score.value = newScore;
}

async function checkIfValid(row, col, value) {
  const numbers = board.value.map((row) => row.map((cell) => cell.value));
  const moveRequestBody = {
    board: numbers,
    row: row,
    col: col,
    value: value,
  };

  const response = await fetch("http://localhost:8080/api/sudoku/isValid", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(moveRequestBody),
  });

  const result = await response.json();
  if (result) {
    updateScore(score.value + 1);
    checkIfSolved();
    console.log(
      "Valid input at row " + row + ", col " + col + " with value " + value,
    );
  } else {
    mistake.value += 1;
    if (mistake.value >= 5) {
      isGameOver.value = true;
    }
    console.log(
      "Invalid input at row " + row + ", col " + col + " with value " + value,
    );
  }
  console.log(result);
}
function userInput(row, col, value) {
  //validate user input
  const number = Number(value);
  if (isNaN(number) || number < 0 || number > 9) {
    board.value[row][col].value = 0;
    return;
  }
  board.value[row][col].value = number;

  if (value != 0) {
    checkIfValid(row, col, number);
  }
}

async function checkIfSolved() {
  const numbers = board.value.map((r) => r.map((cell) => cell.value));
  if (numbers.flat().includes(0)) return;

  const response = await fetch("http://localhost:8080/api/sudoku/isSolved", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(numbers),
  });
  isWon.value = await response.json();
}

async function solveBoard() {
  const original = board.value.map((r) =>
    r.map((cell) => (cell.fixed ? cell.value : 0)),
  );

  const response = await fetch(
    `http://localhost:8080/api/sudoku/solve?puzzleName=${puzzleName.value}`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(original),
    },
  );
  if (!response.ok) return;

  const solved = await response.json();
  board.value = solved.map((r, i) =>
    r.map((value, j) => ({ value, fixed: board.value[i][j].fixed })),
  );
}

onMounted(() => {
  (getBoard(), startTimer());
});
</script>

<template>
  <body>
    <div class="container">
      <div v-if="isGameOver" class="game-over-overlay" @click="reset">
        <div class="modal-content">
          <h1>Game Over!</h1>
          <p>You made 3 mistakes.</p>
          <span class="hint">Click anywhere to restart</span>
        </div>
      </div>
      <div v-if="isWon" class="game-over-overlay" @click="reset">
        <div class="modal-content">
          <h1>You Win!</h1>
          <p>Score: {{ score }} · Time: {{ time }}</p>
          <span class="hint">Click anywhere to play again</span>
        </div>
      </div>
      <title>Sudoku</title>
      <div class="subContainer">
        <label for="timer">Time:</label>
        <input type="text" id="timer" v-model="time" readonly />
      </div>
      <div class="subContainer">
        <label for="score">Score:</label>
        <input type="number" id="score" v-model="score" readonly />
        <label for="mistakes">Mistakes:</label>
        <input type="number" id="mistakes" v-model="mistake" readonly />
      </div>
      <div class="board">
        <div v-for="(row, r) in board" :key="r" class="row">
          <input
            v-for="(cell, c) in row"
            :key="c"
            class="cell"
            :value="cell.value || ''"
            :readonly="cell.fixed"
            @input="userInput(r, c, $event.target.value)"
            size="1"
          />
        </div>
      </div>
      <button class="buttonContainer" @click="reset">Reset</button>
      <button class="buttonContainer" @click="solveBoard">Solve</button>
    </div>
  </body>
</template>
