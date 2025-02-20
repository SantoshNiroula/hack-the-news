package api

// Stories, comments, jobs, Ask HNs and even polls are just items.
// They're identified by their ids, which are unique integers, and live under https://hacker-news.firebaseio.com/v0/item/<id>.
// Only stories, polls and jobs have URLs.
type Item struct {
	// The item's unique
	ID int `json:"id"`

	// true if the item is deleted.
	Deleted bool `json:"deleted"`

	// The type of item. One of "job", "story", "comment", "poll", or "pollopt".
	Type string `json:"type"`

	// The username of the item's author.
	By string `json:"by"`

	// The time of the creation of the item, in Unix Time.
	Time int `json:"time"`

	// The comment, story or poll text. HTML.
	Text string `json:"text"`

	// True if the item is dead.
	Dead bool `json:"dead"`

	// The comment's parent: either another comment or the relevant story.
	Parent int `json:"parent"`

	// Id's of the item's comments, in ranked display order.
	Kids []int `json:"kids"`

	// The URL of the story.
	URL string `json:"url"`

	// The story's score, or the votes for a pollopt.
	Score int `json:"score"`

	// title of the story, poll or job
	Title string `json:"title"`

	// list of related pollopts
	Parts []int `json:"parts"`

	// total comments count
	Descendants int `json:"descendants"`

	Items []Item `json:"items"`
}
