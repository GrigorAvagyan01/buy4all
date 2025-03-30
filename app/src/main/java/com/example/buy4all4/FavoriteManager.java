package com.example.buy4all4;

import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {
    private static FavoriteManager instance;
    private final List<Post> favoritePosts;

    private FavoriteManager() {
        favoritePosts = new ArrayList<>();
    }

    public static FavoriteManager getInstance() {
        if (instance == null) {
            instance = new FavoriteManager();
        }
        return instance;
    }

    public void addFavorite(Post post) {
        if (!favoritePosts.contains(post)) {
            favoritePosts.add(post);
        }
    }

    public void removeFavorite(Post post) {
        favoritePosts.remove(post);
    }

    public List<Post> getFavorites() {
        return favoritePosts;
    }
}