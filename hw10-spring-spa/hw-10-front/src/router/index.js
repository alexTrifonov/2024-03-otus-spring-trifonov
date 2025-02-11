import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../components/HomeView.vue'

const router = createRouter({
  history: createWebHistory('/'),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/authors',
      name: 'author',
      component: () => import('../components/Authors.vue'),
    },
    {
        path: '/genres',
        name: 'genre',
        component: () => import('../components/Genres.vue'),
      },
      {
        path: '/books',
        name: 'books',
        component: () => import('../components/Books.vue'),
      },
      {
        path: '/books/:bookId/comments',
        name: 'comments',
        component: () => import('../components/Comments.vue')
      },
      {
        path: '/books/:bookId/updating',
        name: 'book-updating',
        component: () => import('../components/BookUpdating.vue')
      },
      {
        path: '/books/:bookId/deleting',
        name: 'book-deleting',
        component: () => import('../components/BookDeleting.vue')
      },
      {
        path: '/books/add',
        name: 'add-book',
        component: () => import('../components/AddBook.vue')
      },
      {
        path: '/error',
        name: 'error-page',
        component: () => import('../components/Error.vue')
      }
  ],
})

export default router
