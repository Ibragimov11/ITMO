while (<>) {
	s/\([^\)]*\)/\(\)/g;
	print ;
}