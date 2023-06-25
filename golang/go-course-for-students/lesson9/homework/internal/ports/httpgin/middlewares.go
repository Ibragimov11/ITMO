package httpgin

import (
	"github.com/gin-gonic/gin"
	"log"
	"time"
)

func CustomMW(c *gin.Context) {
	t := time.Now()

	c.Next()

	latency := time.Since(t)
	status := c.Writer.Status()

	log.Println("latency", latency, "method", c.Request.Method, "path", c.Request.URL.Path, "status", status)
}
