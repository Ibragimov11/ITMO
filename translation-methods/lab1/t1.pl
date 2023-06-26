$text_start = 0;
$cur_empty = 0;

while (<>) {
	if (/\S/) {
		$text_start = 1;
	}

	if ($text_start == 1) {
		if (/^\s*$/) {
			$cur_empty = 1;
		} else {
			s/^(\s+)//g;
			s/(\s+)$//g;
			s/(\s+)/ /g;

			if ($cur_empty == 1) {
				print "\n";
			}
			
			print;
			print "\n";	
			$cur_empty = 0;
		}
	}
}