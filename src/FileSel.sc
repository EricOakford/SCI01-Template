;;; Sierra Script 1.0 - (do not remove this comment)
(script# 944)
(include game.sh)
(use Intrface)


(class FileSelector of DSelector
	(properties
		x 13
		y 6
		mask 0
		nFiles 0
	)
	
	(method (init theMask &tmp [temp0 7] temp7 theText temp9)
		(if (> argc 0) (= mask theMask))
		(if (not mask) (= mask {*.*}))
		(if text (Memory 3 text) (= text 0))
		(= nFiles 0)
		(= temp9 (FileIO 8 mask @temp0 0))
		(while temp9
			(++ nFiles)
			(= temp9 (FileIO 9 @temp0))
		)
		(if
		(not (= text (Memory 2 (+ (* nFiles 13) 1))))
			(return 0)
		)
		(= temp7 0)
		(= theText text)
		(= temp9 (FileIO 8 mask @temp0 0))
		(while (and temp9 (< temp7 nFiles))
			(StrCpy theText @temp0)
			(++ temp7)
			(= theText (+ theText 13))
			(= temp9 (FileIO 9 @temp0))
		)
		(StrAt text (* nFiles 13) 0)
		(super init:)
		(return 1)
	)
	
	(method (dispose)
		(if text (Memory 3 text) (= text 0))
		(super dispose:)
	)
	
	(method (setSize &tmp [temp0 4])
		(super setSize:)
		(TextSize @[temp0 0] {M} font)
		(= nsRight (+ nsLeft (* [temp0 3] x)))
	)
)
