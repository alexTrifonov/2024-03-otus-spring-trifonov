<script>
import BooksTable from './BooksTable.vue'
import axios from 'axios'
export default {
    components: {
        BooksTable
    },
    mounted() {
        axios
            .get('/api/books')
            .then((response) => {
                let books = response.data
                for (let aBook of  books) {
                    this.bookData.push({
                        Id: aBook.id,
                        Title: aBook.title,
                        Author: aBook.authorDto.fullName,
                        Genre: aBook.genreDto.name,
                        Comments: aBook.id
                    })
                }
            })
            .catch(
                (error) => this.$router.push('/error')
            )
    },
    data() {
        return {
        bookData: [],
        bookColumns: [
            'Id', 'Title', 'Author', 'Genre'
        ]
        }
    },
    methods: {
        addBook() {
            this.$router.push('/books/add')
        }
    }
}
</script>

<template>
    <div v-if="bookData.length > 0">
        <div>
            <h2>Books:</h2>
        </div>
        <BooksTable :fields="bookColumns" :itemData="bookData"></BooksTable>
    </div>
    <div v-else>
        <h2>No books</h2>
    </div>
    <div>
        <button class="fancy-btn" @click="addBook">Add book</button>
    </div>
</template>