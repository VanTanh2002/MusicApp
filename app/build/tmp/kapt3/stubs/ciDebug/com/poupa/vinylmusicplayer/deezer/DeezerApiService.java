package com.poupa.vinylmusicplayer.deezer;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\b"}, d2 = {"Lcom/poupa/vinylmusicplayer/deezer/DeezerApiService;", "", "getArtistImage", "Lretrofit2/Call;", "Lcom/poupa/vinylmusicplayer/deezer/DeezerResponse;", "artistName", "", "Companion", "app_ciDebug"})
public abstract interface DeezerApiService {
    @org.jetbrains.annotations.NotNull()
    public static final com.poupa.vinylmusicplayer.deezer.DeezerApiService.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "search/artist&limit=1")
    public abstract retrofit2.Call<com.poupa.vinylmusicplayer.deezer.DeezerResponse> getArtistImage(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "q")
    java.lang.String artistName);
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0002J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\bJ\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086\u0002\u00a8\u0006\u000f"}, d2 = {"Lcom/poupa/vinylmusicplayer/deezer/DeezerApiService$Companion;", "", "()V", "createCacheControlInterceptor", "Lokhttp3/Interceptor;", "createDefaultCache", "Lokhttp3/Cache;", "context", "Landroid/content/Context;", "createDefaultOkHttpClient", "Lokhttp3/OkHttpClient$Builder;", "invoke", "Lcom/poupa/vinylmusicplayer/deezer/DeezerApiService;", "client", "Lokhttp3/Call$Factory;", "app_ciDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.poupa.vinylmusicplayer.deezer.DeezerApiService invoke(@org.jetbrains.annotations.NotNull()
        okhttp3.Call.Factory client) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final okhttp3.OkHttpClient.Builder createDefaultOkHttpClient(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        private final okhttp3.Cache createDefaultCache(android.content.Context context) {
            return null;
        }
        
        private final okhttp3.Interceptor createCacheControlInterceptor() {
            return null;
        }
    }
}