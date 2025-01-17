package com.example.t1;

import org.junit.Test;
import static org.junit.Assert.*;

public class PostTest {


    @Test
    public void testPostValidation() {
        Post validPost = new Post("Harun", "MSKÜ", "1234567890", "http://example.com/image.jpg");
        assertTrue(validPost.isValid());

        Post invalidPost = new Post("", "MSKÜ", "1234567890", "http://example.com/image.jpg");
        assertFalse(invalidPost.isValid());
    }
}
