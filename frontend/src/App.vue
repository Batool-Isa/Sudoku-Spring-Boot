<script setup>
import { ref, onMounted } from "vue";

const API_URL = "http://localhost:8080/api/sudoku";
const MAX_MISTAKES = 5;

const score = ref(0);
const board = ref([]);
const mistake = ref(0);
const time = ref("00:00");
const puzzleName = ref("");
const isGameOver = ref(false);
const isWon = ref(false);
const gaveUp = ref(false);
const errorMessage = ref("");
let startTime = Date.now();
let timerId = null;

function startTimer() {
  startTime = Date.now();
  clearInterval(timerId);
  timerId = setInterval(() => {
    if (isGameOver.value || isWon.value || gaveUp.value) return;
    const totalSeconds = Math.floor((Date.now() - startTime) / 1000);
    const m = String(Math.floor(totalSeconds / 60)).padStart(2, "0");
    const s = String(totalSeconds % 60).padStart(2, "0");
    time.value = `${m}:${s}`;
  }, 1000);
}

async function request(path, body) {
  const options = body
    ? {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      }
    : {};
  try {
    const response = await fetch(`${API_URL}${path}`, options);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      errorMessage.value = errorData.message || response.statusText;
      return null;
    }
    errorMessage.value = "";
    return await response.json();
  } catch {
    errorMessage.value = "Can't reach the server. Is the backend running?";
    return null;
  }
}

async function getBoard() {
  const boardData = await request("/loadBoard");
  if (!boardData) return;

  puzzleName.value = boardData.puzzleName;
  board.value = boardData.grid.map((row) =>
    row.map((cell) => ({
      value: cell.value,
      fixed: cell.value !== 0,
      scored: false,
    })),
  );
}

async function reset() {
  isGameOver.value = false;
  isWon.value = false;
  score.value = 0;
  mistake.value = 0;
  time.value = "00:00";
  gaveUp.value = false;
  startTime = Date.now();
  await getBoard();
}

async function checkIfValid(row, col, value) {
  const numbers = board.value.map((r) => r.map((cell) => cell.value));
  const result = await request("/isValid", { board: numbers, row, col, value });
  if (result === null) return;

  const cell = board.value[row][col];
  if (result) {
    if (!cell.scored) {
      score.value += 1;
      cell.scored = true;
    }
    checkIfSolved();
  } else {
    mistake.value += 1;
    if (mistake.value >= MAX_MISTAKES) {
      isGameOver.value = true;
    }
  }
}

function userInput(row, col, event) {
  const value = event.target.value;
  const number = Number(value);
  const cell = board.value[row][col];
  if (value.length > 1 || isNaN(number) || number < 1 || number > 9) {
    cell.value = 0;
    event.target.value = "";
    return;
  }
  if (cell.value !== number) {
    cell.scored = false;
  }
  cell.value = number;
  checkIfValid(row, col, number);
}

async function checkIfSolved() {
  const numbers = board.value.map((r) => r.map((cell) => cell.value));
  if (numbers.flat().includes(0)) return;

  const result = await request("/isSolved", numbers);
  isWon.value = result === true;
}

async function solveBoard() {
  if (!confirm("Give up and see the solution?")) {
    return;
  }

  const original = board.value.map((r) =>
    r.map((cell) => (cell.fixed ? cell.value : 0)),
  );

  const solved = await request(
    `/solve?puzzleName=${encodeURIComponent(puzzleName.value)}`,
    original,
  );
  if (!solved) return;

  board.value = solved.map((r, i) =>
    r.map((value, j) => ({
      value,
      fixed: board.value[i][j].fixed,
      scored: false,
    })),
  );
  gaveUp.value = true;
}

onMounted(() => {
  getBoard();
  startTimer();
});
</script>

<template>
  <div class="container">
    <div v-if="isGameOver" class="game-over-overlay" @click="reset">
      <div class="modal-content">
        <h1>Game Over!</h1>
        <p>You made {{ MAX_MISTAKES }} mistakes.</p>
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
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <div class="board">
      <div v-for="(row, r) in board" :key="r" class="row">
        <input
          v-for="(cell, c) in row"
          :key="c"
          class="cell"
          :value="cell.value || ''"
          :readonly="cell.fixed || gaveUp"
          @input="userInput(r, c, $event)"
          size="1"
        />
      </div>
    </div>
    <button class="buttonContainer" @click="reset">Reset</button>
    <button class="buttonContainer" @click="solveBoard">Gave Up</button>
  </div>
</template>
