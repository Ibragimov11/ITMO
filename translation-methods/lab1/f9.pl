while (<>) {
	print if /^(\S(.*\S)?)?$/;
}