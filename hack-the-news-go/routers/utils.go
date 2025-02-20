package routers

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"
	"strings"

	"github.com/SantoshNiroula/hacker-news/api"
	"github.com/SantoshNiroula/hacker-news/cache"
)

func TopStoryHandler(w http.ResponseWriter, r *http.Request) {
	key := "topstories"
	storyString, err := cache.GetString(key)
	if err == nil && storyString != "" {
		w.Header().Set("Content-Type", "application/json")
		w.Write([]byte(storyString))
		return
	}

	stories, err := api.GetTopStories()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")

	jsonData, err := json.Marshal(stories)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if err := cache.SetString(key, string(jsonData)); err != nil {
		log.Println("Unable to store data in cache")
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(jsonData)

}

func CommentHandler(w http.ResponseWriter, r *http.Request) {
	id := strings.Split(r.URL.Path, "/")[2]
	parsedId, err := strconv.Atoi(id)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	key := fmt.Sprintf("comment-%d", parsedId)
	commentString, err := cache.GetString(key)
	if err == nil && commentString != "" {
		w.Header().Set("Content-Type", "application/json")
		w.Write([]byte(commentString))
		return
	}

	story, err := api.FetchComment(parsedId)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")

	jsonData, err := json.Marshal(story)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if err := cache.SetString(key, string(jsonData)); err != nil {
		log.Println("Unable to store data in cache")
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(jsonData)
}
