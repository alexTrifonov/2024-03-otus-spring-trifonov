<script>
import axios from 'axios'

export default {
    data() {
        return {
            message: '',
            authors: [],
            genres: [],
            title: '',
            authorDto: {},
            genreDto: {},
            book: {}
        }
    },
    mounted() {
        axios
            .get('/api/authors')
            .then((response) => {
                this.authors = response.data
                this.authorDto = this.authors[0]
            })
            .catch(
                (error) => this.$router.push('/error')
            ),
        axios
            .get('/api/genres')
            .then((response) => {
                this.genres = response.data
                this.genreDto = this.genres[0]
            })
            .catch(
                (error) => this.$router.push('/error')
            )
    },
    methods: {
        addBook() {
            axios
                .post('/api/books', {
                    title:  this.title,
                    authorDto:  this.authorDto,
                    genreDto: this.genreDto
                })
                .then((reesponse) => {
                    this.message = 'You have successfully added new book with id = '.concat(reesponse.data.id)
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
  <h2>Adding new book</h2>
  <div v-if="this.message.length == 0">
    <div class="row">
        <label class="green" for="book-title-input">Title:</label>
        <input class="lilac" id="book-title-input" v-model="title" placeholder="Enter title"/>
    </div>
    <div class="row">
        <label class="green" for="author-select">Author:</label>
        <select id="author" class="box" v-model="this.authorDto">
        <option v-for="author in authors" :value="author">{{ author.fullName }}</option>
        </select>
    </div class="row">
        <label class="green" for="genre-select">Genre:</label>
        <select id="genre-select" class="box" v-model="this.genreDto">
            <option v-for="genre in genres" :value="genre">{{ genre.name }}</option>
        </select>
    <div class="row">
        <button class="fancy-btn" @click="addBook">Add new book</button>
        <button class="fancy-btn" @click="goToBooks">Back to books</button>
    </div>
  </div>
  <div v-else>
    {{ message }}
  </div>
</template>
