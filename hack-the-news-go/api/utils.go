package api

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"sync"
	"time"
)

var (
	counts   = 50
	comments = 20
)

func GetTopStories() ([]Item, error) {
	var stories []Item

	client := http.DefaultClient
	response, err := client.Get("https://hacker-news.firebaseio.com/v0/topstories.json")
	if err != nil {
		return stories, err
	}

	var storyIDs []int
	err = json.NewDecoder(response.Body).Decode(&storyIDs)
	if err != nil {
		return stories, err
	}

	result := make(chan Item, counts)
	start := time.Now()
	var wg sync.WaitGroup

	wg.Add(counts)
	for i := range counts {
		go fetchItemByID(result, storyIDs[i], &wg)
	}

	wg.Wait()

	for i := 0; i < counts; i++ {
		stories = append(stories, <-result)
	}

	end := time.Since(start)
	fmt.Printf("Time taken: %s\n", end)

	fmt.Println(len(stories))

	return stories, nil
}

func fetchItemByID(result chan<- Item, id int, wg *sync.WaitGroup) {
	defer wg.Done()

	client := http.DefaultClient
	response, err := client.Get(fmt.Sprintf("https://hacker-news.firebaseio.com/v0/item/%d.json", id))
	if err != nil {
		log.Println(err)
		return
	}

	defer response.Body.Close()

	var item Item
	err = json.NewDecoder(response.Body).Decode(&item)
	if err != nil {
		log.Println(err)
		return
	}

	result <- item
}

func FetchComment(id int) (Item, error) {
	var wgs sync.WaitGroup
	result := make(chan Item, 1)

	wgs.Add(1)
	go fetchCommentRecursive(result, id, &wgs)
	wgs.Wait()

	item := <-result
	return item, nil

}

func fetchItemByID2(id int) (Item, error) {
	var item Item
	client := http.DefaultClient
	response, err := client.Get(fmt.Sprintf("https://hacker-news.firebaseio.com/v0/item/%d.json", id))
	if err != nil {
		return item, err
	}

	err = json.NewDecoder(response.Body).Decode(&item)
	response.Body.Close()
	if err != nil {
		fmt.Printf("Error: %d\n", id)
		return item, err
	}

	return item, nil

}

func fetchCommentRecursive(result chan<- Item, id int, wg *sync.WaitGroup) {

	defer wg.Done()

	item, err := fetchItemByID2(id)
	if err != nil {
		return
	}

	if item.Descendants == 0 {
		result <- item
		return
	}

	var maxCommentToFetch int
	if item.Descendants > comments {
		maxCommentToFetch = comments
	} else {
		maxCommentToFetch = item.Descendants
	}

	var wgs sync.WaitGroup
	results := make(chan Item, maxCommentToFetch)

	wgs.Add(maxCommentToFetch)
	for _, kid := range item.Kids[:maxCommentToFetch] {
		go fetchCommentRecursive(results, kid, &wgs)
	}

	wgs.Wait()

	for i := 0; i < maxCommentToFetch; i++ {
		item.Items = append(item.Items, <-results)
	}

	result <- item
}
