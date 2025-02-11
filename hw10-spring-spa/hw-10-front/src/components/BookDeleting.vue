<script>
import axios from 'axios'

export default {
    data() {
        return {
            id: this.$route.params.bookId
        }
    },
    methods: {
        deleteBook() {
            axios.delete('/api/books/'.concat(this.id))
            .then((response) => {
                this.$router.push('/books')
            })
            .catch(
                (error) => this.$router.push('/error')
            )
        },
        goToBooks() {
            this.$router.push('/books')
        },
    }
}


</script>

<template>
  <div> 
    <h2 class="lilac">Are you sure?</h2>
  <h2 class="lilac">Do you want to delete the book with id = {{ this.id }}?</h2>
    <div>
        <button class="fancy-btn" @click="deleteBook">Delete book</button>
        <button class="fancy-btn" @click="goToBooks">Back to books</button>
    </div>
  </div>
</template>
