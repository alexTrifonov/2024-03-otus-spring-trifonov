<script>
import axios from 'axios'

export default {
    data() {
        return {
            id: this.$route.params.bookId,
            authors: [],
            genres: [],
            title: '',
            authorDto: {},
            genreDto: {},
            book: {}
        }
    },
    async mounted() {
        await axios
            .get('/api/books/'.concat(this.id))
            .then((response) => {
                this.book = response.data
                this.title = this.book.title
            })
            .catch(
                (error) => this.$router.push('/error')
            )

        axios
            .get('/api/authors')
            .then((response) => {
                this.authors = response.data
                this.authorDto = this.book.authorDto
            })
            .catch(
                (error) => this.$router.push('/error')
            ),

        axios
            .get('/api/genres')
            .then((response) => {
                this.genres = response.data
                this.genreDto = this.book.genreDto
            })
            .catch(
                (error) => this.$router.push('/error')
            )
    },
    methods: {
        updateBook() {
            axios
                .put('/api/books/'.concat(this.id), {
                    id: this.id,
                    title:  this.title,
                    authorDto:  this.authorDto,
                    genreDto: this.genreDto
                })
                .then((reesponse) => {
                    this.$router.push('/books')
                })
                .catch(
                    (error) => this.$router.push('/error')
                )
        },
        goToBooks() {
            this.$router.push('/books')
        }
    }
}
</script>

<template>
  <h2>Book updating with id = {{ $route.params.bookId }}</h2>
  <div>
    <div>
        <label class="green" for="title-input">Title</label>
        <input class="lilac" v-model="title" placeholder="Enter title"/>
    </div>
    <div class="row">
        <label class="green" for="author-select">Author:</label>
        <select id="author-select" class="box" v-model="this.authorDto">
            <option v-for="author in authors" :value="author">{{ author.fullName }}</option>
        </select>
    </div>
    <div class="row">
        <label class="green" for="genre-select">Genre</label>
        <select id="genre-select" class="box" v-model="this.genreDto">
            <option v-for="genre in genres" :value="genre">{{ genre.name }}</option>
        </select>
    </div>
    
    <div class="row">
        <button class="fancy-btn" @click="updateBook">Update book</button>
        <button class="fancy-btn" @click="goToBooks">Back to books</button>
    </div>
  </div>
</template>
