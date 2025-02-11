<script>
import Table from './Table.vue'
import axios from 'axios'
export default {
    components: {
        Table
    },
    mounted() {
        this.urlComments = this.urlComments.concat(this.$route.params.bookId)
        axios.get(this.urlComments)
        .then((response) => {
            let comments = response.data
            for (let comment of  comments) {
                this.commentsData.push({
                    Id: comment.id,
                    Text: comment.text
                })
            }
        })
    },
  data() {
    return {
      commentsData: [],
      commentsColomns: [
        'Id', 'Text'
      ],
      id: this.$route.params.bookId,
      urlComments: '/api/comments?bookId='
    }
  }
}
</script>

<template>
    <div>
        <h2>Comments for book with id = {{ $route.params.bookId }}:</h2>
    </div>
    <div v-if="commentsData.length > 0">
        <Table :fields="commentsColomns" :itemData="commentsData"></Table>
    </div>
    <div v-else>
        <h2 class="lilac">No comments</h2>
    </div>
</template>