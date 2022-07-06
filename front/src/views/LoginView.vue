<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const username = ref("");
const password = ref("");

const home = () => {
  router.push({ name: "home" });
};

const login = () => {
  axios
    .post("/api/login", {
      id: username.value,
      pwd: password.value,
    })
    .then(() => {
      router.replace({ name: "home" });
    });
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
    <div class="mt-2">
      <div class="d-flex justify-content-center">
        <el-button type="warning" @click="login()">로그인</el-button>
        <el-button type="info" @click="home()">홈</el-button>
      </div>
    </div>
  </el-card>
</template>
