<template>
  <div id="app">
    <Header_page></Header_page>
  </div>
</template>

<script>
import Header_page from "@/components/header_page";
export default {
  name: 'App',
  components: {
    Header_page

  },
  created() {
  }
}
</script>

<style>
#app {
  /*font-family: Avenir, Helvetica, Arial, sans-serif;*/
  /*-webkit-font-smoothing: antialiased;*/
  /*-moz-osx-font-smoothing: grayscale;*/
  /*text-align: center;*/
  /*color: #2c3e50;*/
  /*margin-top: 60px;*/
}
</style>
