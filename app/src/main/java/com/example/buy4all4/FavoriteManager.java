package com.example.buy4all4;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteManager {
    private static final String PREF_NAME = "favorites_pref";
    private static final String KEY_FAVORITES = "favorite_post_ids";

    private static FavoriteManager instance;
    private final SharedPreferences sharedPreferences;
    private final Set<String> favoritePostIds;
    private final List<Post> favoritePosts;

    private FavoriteManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        favoritePostIds = new HashSet<>(sharedPreferences.getStringSet(KEY_FAVORITES, new HashSet<>()));
        favoritePosts = new ArrayList<>();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new FavoriteManager(context);
        }
    }

    public static FavoriteManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Call FavoriteManager.init(context) before using getInstance()");
        }
        return instance;
    }

    public void addFavorite(Post post) {
        if (!favoritePostIds.contains(post.getPostId())) {
            favoritePostIds.add(post.getPostId());
            saveFavorites();
            favoritePosts.add(post);
        }
    }

    public void removeFavorite(Post post) {
        if (favoritePostIds.remove(post.getPostId())) {
            saveFavorites();
            favoritePosts.remove(post);
        }
    }

    public boolean isFavorite(Post post) {
        return favoritePostIds.contains(post.getPostId());
    }

    public void toggleFavorite(Post post) {
        if (isFavorite(post)) {
            removeFavorite(post);
        } else {
            addFavorite(post);
        }
    }

    public List<Post> getFavorites() {
        return new ArrayList<>(favoritePosts);
    }

    public Set<String> getFavoritePostIds() {
        return new HashSet<>(favoritePostIds);
    }

    private void saveFavorites() {
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, favoritePostIds).apply();
    }
}
