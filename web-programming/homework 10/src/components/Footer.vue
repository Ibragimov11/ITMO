<template>
    <footer>
        <div>
            Users: {{countUsers}}
            Posts: {{countPosts}}
        </div>
        <a href="/">Codeforces</a> &copy; 2021 by Mike Mirzayanov
    </footer>
</template>

<script>
export default {
    name: "Footer",
    props: ["countUsers", "countPosts"]
}
</script>

<style scoped>

</style>