<script>
import Table from './Table.vue'
import axios from 'axios'
export default {
    components: {
        Table
    },
    mounted() {
        axios
            .get('/api/genres')
            .then((response) => {
                this.genreData = response.data
                this.genreColumns = Object.keys(this.genreData[0])
            })
            .catch(
                (error) => this.$router.push('/error')
            )
    },
  data() {
    return {
      genreData: [],
      genreColumns: []
    }
  }
}
</script>

<template>
    <div>
        <h2>Genres:</h2>
    </div>
    <div>
        <Table :fields="genreColumns" :itemData="genreData"></Table>
    </div>
</template>