<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
const username = ref("");
const password = ref("");
const passwordchk = ref("");
const router = useRouter();
const join = function () {
  axios
    .post("/api/join", {
      username: username.value,
      password: password.value,
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
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>Log-in</span>
      </div>
    </template>
    <el-input
      v-model="username"
      placeholder="Please input username"
      clearable
    />
    <el-input
      v-model="password"
      type="password"
      placeholder="Please input password"
      show-password
    />
    <el-input
      v-model="passwordchk"
      type="passwordchk"
      placeholder="Please check password"
      show-password
    />
    <div class="mt-2">
      <div class="d-flex justify-content-center">
        <el-button type="warning" @click="join()">회원가입</el-button>
        <el-button type="info" @click="home()">취소</el-button>
      </div>
    </div>
  </el-card>
</template>

<style></style>
