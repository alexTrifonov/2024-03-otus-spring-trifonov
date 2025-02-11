<script>
import Table from './Table.vue'
import axios from 'axios'
export default {
    components: {
        Table
    },
    mounted() {
        axios
        .get('/api/authors')
        .then((response) => {
            this.authorsData = response.data
            this.authorColomns = Object.keys(this.authorsData[0])
        })
        .catch(
            (error) => this.$router.push('/error')
        )
    },
  data() {
    return {
      authorsData: [],
      authorColomns: []
    }
  }
}
</script>

<template>
    <div>
        <h2>Authors:</h2>
    </div>
    <div>
        <Table :fields="authorColomns" :itemData="authorsData"></Table>
    </div>
</template>