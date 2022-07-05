<script setup lang="ts">
import { ref } from "vue";

import axios from "axios";
import { useRouter } from "vue-router";

const title = ref("");
const content = ref("");

const router = useRouter();

const write = function () {
  axios
    .post("/api/posts", {
      title: title.value,
      content: content.value,
    })
    .then(() => {
      router.replace({ name: "home" }); // index.ts -> routes -> name
    });
};

const home = () => {
  router.replace({ name: "home" });
};
</script>
<template>
  <div>
    <el-input
      type="text"
      v-model="title"
      placeholder="제목을 입력해주세요."
      maxlength="100"
      show-word-limit
    />
  </div>

  <div class="mt-2">
    <el-input type="textarea" v-model="content" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">글 작성 완료</el-button>
      <el-button type="info" @click="home()">Home</el-button>
    </div>
  </div>
</template>

<style></style>
