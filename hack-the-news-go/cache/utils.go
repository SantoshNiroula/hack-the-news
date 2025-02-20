package cache

import (
	"context"
	"time"

	"github.com/redis/go-redis/v9"
)

func SetString(key string, value string) error {
	client := redis.NewClient(&redis.Options{})
	return client.Set(context.Background(), key, value, time.Hour).Err()
}

func GetString(key string) (string, error) {
	client := redis.NewClient(&redis.Options{})
	return client.Get(context.Background(), key).Result()
}
