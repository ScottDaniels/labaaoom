# master mk file
# assumes that:
#	mk is used (plan 9 mk by Andrew Hume)
#	xfig and xfig tools are installed
#	{X}fm is installed

fonts = -I/usr/local/share/ghostscript/fonts 
%.eps:: %.fig
	fig2dev -L eps $prereq $target

%.pdf:: %.ps
	gs $fonts -dBATCH  -dNOPROMPT -dNOPAUSE -sDEVICE=pdfwrite -sOutputFile=$target ${prereq%% *}

%.ps :: %.xfm
	pfm ${prereq%% *} $target

$ALL::

# general clean up, but leaves doc files
clean:V:
	rm -f *.ps  *.ca

# trasshes everything that can be built, including documents
nuke:V: clean
	rm -f *.pdf

