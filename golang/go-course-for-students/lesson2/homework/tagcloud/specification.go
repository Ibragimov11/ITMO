package tagcloud

import (
	"sort"
)

// TagCloud aggregates statistics about used tags
type TagCloud struct {
	tagStats map[string]int
}

// TagStat represents statistics regarding single tag
type TagStat struct {
	Tag             string
	OccurrenceCount int
}

// New should create a valid TagCloud instance
func New() TagCloud {
	return TagCloud{
		tagStats: map[string]int{},
	}
}

// AddTag should add a tag to the cloud if it wasn't present and increase tag occurrence count
// thread-safety is not needed
func (tagCloud *TagCloud) AddTag(tag string) {
	tagCloud.tagStats[tag]++
}

// TopN should return top N most frequent tags ordered in descending order by occurrence count
// if there are multiple tags with the same occurrence count then the order is defined by implementation
// if n is greater that TagCloud size then all elements should be returned
// thread-safety is not needed
// there are no restrictions on time complexity
func (tagCloud *TagCloud) TopN(n int) []TagStat {
	var tagStatsSlice []TagStat

	for tag, stat := range tagCloud.tagStats {
		tagStatsSlice = append(tagStatsSlice, TagStat{tag, stat})
	}

	sort.Slice(tagStatsSlice, func(i, j int) bool {
		return tagStatsSlice[i].OccurrenceCount > tagStatsSlice[j].OccurrenceCount
	})

	if n < len(tagStatsSlice) {
		return tagStatsSlice[:n]
	}

	return tagStatsSlice
}
