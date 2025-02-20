package main

import (
	"log"
	"net/http"

	"github.com/SantoshNiroula/hacker-news/routers"
)

func main() {

	m := http.NewServeMux()

	m.HandleFunc("/topstories/", routers.TopStoryHandler)
	m.HandleFunc("/topstories/{id}/", routers.CommentHandler)

	if err := http.ListenAndServe(":8080", m); err != nil {
		log.Panicf("Error occurred %s", err)
	}

}
