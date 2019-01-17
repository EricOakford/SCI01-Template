;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  FILESEL.SC
;;;;  (c) Sierra On-Line, Inc, 1990
;;;;
;;;;  Author: Mark Wilden
;;;;
;;;;  Type of selector allowing user to select from a list of file names
;;;;  matching a mask
;;;;  readFiles: method returns 0 if unable to hold all file names in memory
;;;;
;;;;  MS-DOS specific

(script# FILESEL)
(include system.sh) (include sci2.sh)
(use Intrface)


(class FileSelector of DSelector
	(properties
		type $0006
		state $0000
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		font 0
		x 13
		y 6
		text 0		; array of file names matching mask
		cursor 0
		lsTop 0
		mark 0
		mask 0		; file selection mask, e.g. *.* or c:\sierra\tmp\*.sg
		nFiles 0	; number of files matching mask

	)
	
	(method (init theMask &tmp [fileName 7] i cp rc)
		(if (> argc 0) (= mask theMask))
		(if (not mask) (= mask {*.*}))
		(if text (Memory MDisposePtr text) (= text 0)
			)
			; Choose all "normal" files, since the attribute matching of this call
			; is severely brain-damaged.
		(= nFiles 0)
		(= rc (FileIO fileFindFirst mask @fileName 0))
		(while rc
			(++ nFiles)
			(= rc (FileIO fileFindNext @fileName))
		)
		
		; allocate the memory for array
		(if
		(not (= text (Memory MNewPtr (+ (* nFiles 13) 1))))
			(return 0)
		)
		; go through the directory again and actually add files to array
		(= i 0)
		(= cp text)
		(= rc (FileIO fileFindFirst mask @fileName 0))
		(while (and rc (< i nFiles))
			(StrCpy cp @fileName)
			(++ i)
			(= cp (+ cp maxFileName))
			(= rc (FileIO fileFindNext @fileName))
		)
		(StrAt text (* nFiles 13) 0)
		(super init:)
		(return TRUE)
	)
	
	(method (dispose)
		(if text (Memory MDisposePtr text)
			(= text 0)
			)
		(super dispose:)
	)
	
	(method (setSize &tmp [r 4])
		(super setSize:)
		(TextSize @[r 0] {M} font)
		(= nsRight (+ nsLeft (* [r 3] x)))
	)
)
