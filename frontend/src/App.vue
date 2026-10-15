<script setup>
import { ref, onMounted } from 'vue'

const score = ref(0)
const name = ref('')
const board = ref([])

async function getBoard(){
    const response = await fetch('http://localhost:8080/api/sudoku/loadBoard')
    board.value = await response.json()
}
onMounted(getBoard)


</script>

<template>
    <body>
  <!-- events -->
  <button id="loadButton" @click="getBoard">ReloadBoard</button>

  <div v-for="(row, r) in board" :key="r" class="row">
   <input v-for="(cell, c) in row" :key="c" class="cell"
       :value="cell.value || ''"
       :readonly="cell.fixed"
       size="1">
  </div>
</body>
</template>