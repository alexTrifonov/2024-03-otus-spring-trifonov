package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraphComment = em.getEntityGraph("comment-book-graph");
        return Optional.ofNullable(em.find(Comment.class, id,
                Collections.singletonMap(FETCH.getKey(), entityGraphComment)));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBookId(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        Map<String, Object> properties = new HashMap<>();
        properties.put(FETCH.getKey(), entityGraph);
        em.find(Book.class, id, properties);

        var queryComment = em.createQuery("select c from Comment c where c.book.id = :id", Comment.class);
        queryComment.setParameter("id", id);
        return queryComment.getResultList();
    }
}
