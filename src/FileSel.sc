;;; Sierra Script 1.0 - (do not remove this comment)
(script# FILESEL)
(include game.sh)
(use Intrface)


(class FileSelector of DSelector
	(properties
		x 13
		y 6
		mask 0
		nFiles 0
	)
	
	(method (init theMask &tmp [fileName 7] i cp rc)
		(if (> argc 0) (= mask theMask))
		(if (not mask) (= mask {*.*}))
		(if text (Memory MDisposePtr text) (= text 0))
		(= nFiles 0)
		(= rc (FileIO fileFindFirst mask @fileName 0))
		(while rc
			(++ nFiles)
			(= rc (FileIO fileFindNext @fileName))
		)
		(if
		(not (= text (Memory MNewPtr (+ (* nFiles maxFileName) 1))))
			(return 0)
		)
		(= i 0)
		(= cp text)
		(= rc (FileIO fileFindFirst mask @fileName 0))
		(while (and rc (< i nFiles))
			(StrCpy cp @fileName)
			(++ i)
			(= cp (+ cp maxFileName))
			(= rc (FileIO fileFindNext @fileName))
		)
		(StrAt text (* nFiles maxFileName) 0)
		(super init:)
		(return 1)
	)
	
	(method (dispose)
		(if text (Memory MDisposePtr text) (= text 0))
		(super dispose:)
	)
	
	(method (setSize &tmp [r 4])
		(super setSize:)
		(TextSize @[r 0] {M} font)
		(= nsRight (+ nsLeft (* [r 3] x)))
	)
)
