;;; Sierra Script 1.0 - (do not remove this comment)
(script# SAVE)
(include game.sh)
(use Main)
(use Intrface)
(use System)

(define  GAMESSHOWN 8)     ;the number of games displayed in the selector
(define  MAXGAMES 20)      ;maximum number of games in a save directory
(define  COMMENTSIZE 36)   ;size of user's description of the game
(define  COMMENTBUFF 18) 	;(/ (+ 1 COMMENTSIZE) 2))

(define  DIRECTORYSIZE 29) ;size of the save directory name
(define  DIRECTORYBUFF 15)	;(/ (+ 1 DIRECTORYSIZE) 2))

(define  BUFFERSIZE 361)	;(+ (* MAXGAMES COMMENTBUFF) 1))

;;;(procedure
;;;   GetDirectory
;;;   HaveSpace
;;;   GetStatus
;;;   NeedDescription
;;;)

(public
   GetDirectory   0
)


(local
	default
	i
	numGames
	selected
	theStatus
	[butbuf1 4] = [{Restore} {__Save__} {Replace} {Replace}]
	[butbuf2 4] = [{Select the game that you would like to restore.} {Type the description of this saved game.}
	{This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.}
	{This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.}]
)

(enum
   RESTORE        ;Restore games
   HAVESPACE      ;Save, with space on disk
   NOSPACE        ;Save, no space on disk but games to replace
   NOREPLACE      ;Save, no space on disk, no games to replace
)

(class SysWindow of Object
	(properties
		top 0
		left 0
		bottom 0
		right 0
		color 0
		back 15
		priority -1
		window 0
		type $0000
		title 0
		brTop 0
		brLeft 0
		brBottom 190
		brRight 320
	)
	
	(method (dispose)
		(if window (DisposeWindow window) (= window 0))
		(super dispose:)
	)
	
	(method (open)
		(= window
			(NewWindow
				top
				left
				bottom
				right
				title
				type
				priority
				color
				back
			)
		)
	)
)

(class SRDialog of Dialog
   ;;; The SRDialog class implements the user interface for save/restore.
   ;;; Its subclasses are the specific save and restore game dialogs,
   ;;; Save and Restore.
	
	(method (init theComment names nums)
		
      ;; Initialize the dialog.

      ; give ourself the system window as our window
      (= window SysWindow)

      ;Re-init our size, with no elements.
      (= nsBottom 0)

      ;Get some files for this directory.
      (= numGames (GetSaveFiles (theGame name?) names nums))
      (if (== numGames -1)
         (return FALSE)
      )

      (= theStatus (GetStatus))

      ;Set up the edit item for saved games.
      (if (== theStatus HAVESPACE)
         (editI
            text: (StrCpy theComment names),
            font: smallFont,
            setSize:,
            moveTo: MARGIN MARGIN
         )
         (self add: editI, setSize:)
      )

      ;Set up the selectorI box.
      (selectorI
         text: names,
         font: smallFont,
         setSize:,
         moveTo: MARGIN (+ nsBottom MARGIN),
         state: dExit
      )
      (= i (+ (selectorI nsRight?) MARGIN))
     	(okI
			text: [butbuf1 theStatus]
			setSize:
			moveTo: i (selectorI nsTop?)
			state: (if (== theStatus NOREPLACE) 0 else (| dActive dExit))
		)
		(cancelI
			setSize:
			moveTo: i (+ (okI nsBottom?) MARGIN)
			state: (& (cancelI state?) (~ dSelected))
		)
		(changeDirI
			setSize:
			moveTo: i (+ (cancelI nsBottom?) MARGIN)
			state: (& (changeDirI state?) (~ dSelected))
		)
		(self add: selectorI okI cancelI changeDirI setSize:)
		(textI
			text: [butbuf2 theStatus]
			setSize: (- (- nsRight nsLeft) (* 2 MARGIN))
			moveTo: MARGIN MARGIN
		)
		(= i (+ (textI nsBottom?) 4))
		(self eachElementDo: #move 0 i)
		(self add: textI setSize: center: open: wTitled 15)
		(return 1)
	)
	
;EO: this is adapted from SCI16's SAVE.SC. It doesn't fully match the commented 
;assembly code below.

   (method  (doit theComment
                  &tmp  fd ret offset 
                        [names 361] [nums 21]
                        [str 100] [dir 40]
            )

      ;If restore: is called with a TRUE parameter, do nothing if there
      ;are no saved games.  This allows optionally presenting the user
      ;with his saved games at the start of the game.
      (if
         (and
            (== self Restore)
            argc
            theComment
         )

         (= fd (FileIO fileOpen (Format @str SAVE 0 (theGame name?))))
         (if (== fd -1)
            ;no directory -> no saved games
            (return)
         )
         (FileIO fileClose fd)
      )

      (if (not (self init: theComment @names @nums))
         (return -1)
      )

      (repeat
         (= default
            (switch theStatus
               (RESTORE
                  (if numGames okI else changeDirI)
               )
               (HAVESPACE
                  ;Edit item of save games is active if present
                  editI
               )
               (NOSPACE
                  ;If there are save-games to replace, 'Replace'
                  ;button is active.
                  okI
               )
               (else
                  ;Otherwise 'Change Directory' button is active.
                  changeDirI
               )
            )
         )

         (= i (super doit: default))

         (= selected (selectorI indexOf: (selectorI cursor?)))
         (= offset (* selected (/ (+ 1 COMMENTSIZE) 2)))
         (cond
            ((== i changeDirI)
               ;; kill save window to save hunk
               (self dispose:)
               (if (GetDirectory curSaveDir)
                  (= numGames
                     (GetSaveFiles (theGame name?) @names @nums)
                  )
                  (if (== numGames -1)
                     (= ret -1)
                     (break)
                  )
               )
               ;; open save back up with new directory
               (self init: theComment @names @nums)
            )

            ((and (== theStatus NOSPACE) (== i okI))
               (self dispose:)
               (if (GetReplaceName doit: (StrCpy theComment @[names offset]))
                  (= ret [nums selected])
                  (break)
               )
               (self init: theComment @names @nums)
            )

            ((and (== theStatus HAVESPACE) (or (== i okI) (== i editI)))
               (if (== (StrLen theComment) 0)
                  (self dispose:)
                  (NeedDescription)
                  (self init: theComment @names @nums)
                  (continue)
               )

               (= ret -1)
               (for  ((= i 0))
                     (< i numGames)
                     ((++ i))

                  (= ret (StrCmp theComment @[names (* i (/ (+ 1 COMMENTSIZE) 2))]))
                  (breakif (not ret))
               )

               (cond
                  ((not ret)
                     (= ret [nums i])
                  )
                  ((== numGames MAXGAMES)
                     (= ret [nums selected])
                  )
                  (else
                     ; find the lowest unused game number
                     (for ((= ret 0)) TRUE ((++ ret))
                        (for ((= i 0)) (< i numGames) ((++ i))
                           (breakif (== ret [nums i])) ; this number is used
                        )
                        (if (== i numGames)  ; checked all entries in nums
                           (break)           ; and none matched
                        )
                     )
                  )
               )
               (break)
            )
            ((== i okI)
               (= ret [nums selected])
               (break)
            )

            ((or (== i 0) (== i cancelI))	;EO: changed from -1 so that pressing ESC exits the window.
               (= ret -1)
               (break)
            )

            ((== theStatus HAVESPACE)
               (editI
                  cursor:
                     (StrLen (StrCpy theComment @[names offset])),
                  draw:
               )
            )
         )
      )
      (self dispose:)
      (return ret)
   )
)
;EO: Here is the disassembled doit

;;;	(method (doit theComment &tmp fd printRet offset temp3 [names BUFFERSIZE] [nums 21] [str 40])
;;;		(asm
;;;			pushSelf
;;;			class    Restore
;;;			eq?     
;;;			bnt      code_026e
;;;			lap      argc
;;;			bnt      code_026e
;;;			lap      theComment
;;;			bnt      code_026e
;;;			pushi    2
;;;			pushi    0
;;;			pushi    4
;;;			lea      @str
;;;			push    
;;;			pushi    990
;;;			pushi    0
;;;			pushi    #name
;;;			pushi    0
;;;			lag      theGame
;;;			send     4
;;;			push    
;;;			callk    Format,  8
;;;			push    
;;;			callk    FileIO,  4
;;;			sat      printRet
;;;			push    
;;;			ldi      65535
;;;			eq?     
;;;			bnt      code_0267
;;;			ret     
;;;code_0267:
;;;			pushi    2
;;;			pushi    1
;;;			lst      printRet
;;;			callk    FileIO,  4
;;;code_026e:
;;;			pushi    #init
;;;			pushi    3
;;;			lsp      theComment
;;;			lea      @names
;;;			push    
;;;			lea      @nums
;;;			push    
;;;			self     10
;;;			not     
;;;			bnt      code_0287
;;;			ldi      65535
;;;			ret     
;;;code_0287:
;;;			lsl      theStatus
;;;			dup     
;;;			ldi      0
;;;			eq?     
;;;			bnt      code_02a1
;;;			lal      numGames
;;;			bnt      code_02be
;;;			lofsa    okI
;;;			jmp      code_02be
;;;			lofsa    changeDirI
;;;			jmp      code_02be
;;;code_02a1:
;;;			dup     
;;;			ldi      1
;;;			eq?     
;;;			bnt      code_02ae
;;;			lofsa    editI
;;;			jmp      code_02be
;;;code_02ae:
;;;			dup     
;;;			ldi      2
;;;			eq?     
;;;			bnt      code_02bb
;;;			lofsa    okI
;;;			jmp      code_02be
;;;code_02bb:
;;;			lofsa    changeDirI
;;;code_02be:
;;;			toss    
;;;			sal      default
;;;			pushi    #doit
;;;			pushi    1
;;;			push    
;;;			super    Dialog,  6
;;;			sal      i
;;;			pushi    #indexOf
;;;			pushi    1
;;;			pushi    #cursor
;;;			pushi    0
;;;			lofsa    selectorI
;;;			send     4
;;;			push    
;;;			lofsa    selectorI
;;;			send     6
;;;			sal      selected
;;;			push    
;;;			ldi      18
;;;			mul     
;;;			sat      temp3
;;;			lsl      i
;;;			lofsa    changeDirI
;;;			eq?     
;;;			bnt      code_0389
;;;			pushi    1
;;;			lsg      curSaveDir
;;;			call     GetDirectory,  2
;;;			bnt      code_0287
;;;			pushi    3
;;;			pushi    #name
;;;			pushi    0
;;;			lag      theGame
;;;			send     4
;;;			push    
;;;			lea      @names
;;;			push    
;;;			lea      @nums
;;;			push    
;;;			callk    GetSaveFiles,  6
;;;			sal      numGames
;;;			push    
;;;			ldi      65535
;;;			eq?     
;;;			bnt      code_031d
;;;			ldi      65535
;;;			sat      offset
;;;			jmp      code_0499
;;;code_031d:
;;;			lal      theStatus
;;;			sat      fd
;;;			pushi    0
;;;			call     GetStatus,  0
;;;			sal      theStatus
;;;			push    
;;;			dup     
;;;			ldi      0
;;;			eq?     
;;;			bnt      code_0333
;;;			jmp      code_0379
;;;code_0333:
;;;			dup     
;;;			lat      fd
;;;			eq?     
;;;			bnt      code_0364
;;;			pushi    #contains
;;;			pushi    1
;;;			lofsa    editI
;;;			push    
;;;			self     6
;;;			bnt      code_0379
;;;			pushi    #cursor
;;;			pushi    1
;;;			pushi    1
;;;			pushi    2
;;;			lsp      theComment
;;;			lea      @names
;;;			push    
;;;			callk    StrCpy,  4
;;;			push    
;;;			callk    StrLen,  2
;;;			push    
;;;			pushi    83
;;;			pushi    0
;;;			lofsa    editI
;;;			send     10
;;;			jmp      code_0379
;;;code_0364:
;;;			pushi    #dispose
;;;			pushi    0
;;;			pushi    102
;;;			pushi    3
;;;			lsp      theComment
;;;			lea      @names
;;;			push    
;;;			lea      @nums
;;;			push    
;;;			self     14
;;;code_0379:
;;;			toss    
;;;			pushi    #setSize
;;;			pushi    0
;;;			pushi    83
;;;			pushi    0
;;;			lofsa    selectorI
;;;			send     8
;;;			jmp      code_0287
;;;code_0389:
;;;			lsl      theStatus
;;;			ldi      2
;;;			eq?     
;;;			bnt      code_03bf
;;;			lsl      i
;;;			lofsa    okI
;;;			eq?     
;;;			bnt      code_03bf
;;;			pushi    #doit
;;;			pushi    1
;;;			pushi    2
;;;			lsp      theComment
;;;			lat      temp3
;;;			leai     @names
;;;			push    
;;;			callk    StrCpy,  4
;;;			push    
;;;			lofsa    GetReplaceName
;;;			send     6
;;;			bnt      code_0287
;;;			lal      selected
;;;			lati     nums
;;;			sat      offset
;;;			jmp      code_0499
;;;			jmp      code_0287
;;;code_03bf:
;;;			lsl      theStatus
;;;			ldi      1
;;;			eq?     
;;;			bnt      code_0440
;;;			lsl      i
;;;			lofsa    okI
;;;			eq?     
;;;			bt       code_03d9
;;;			lsl      i
;;;			lofsa    editI
;;;			eq?     
;;;			bnt      code_0440
;;;code_03d9:
;;;			pushi    1
;;;			lsp      theComment
;;;			callk    StrLen,  2
;;;			push    
;;;			ldi      0
;;;			eq?     
;;;			bnt      code_03ee
;;;			pushi    0
;;;			call     NeedDescription,  0
;;;			jmp      code_0287
;;;code_03ee:
;;;			ldi      65535
;;;			sat      offset
;;;			ldi      0
;;;			sal      i
;;;code_03f6:
;;;			lsl      i
;;;			lal      numGames
;;;			lt?     
;;;			bnt      code_0418
;;;			pushi    2
;;;			lsp      theComment
;;;			lsl      i
;;;			ldi      18
;;;			mul     
;;;			leai     @names
;;;			push    
;;;			callk    StrCmp,  4
;;;			sat      offset
;;;			not     
;;;			bnt      code_0413
;;;code_0413:
;;;			+al      i
;;;			jmp      code_03f6
;;;code_0418:
;;;			lat      offset
;;;			not     
;;;			bnt      code_0426
;;;			lal      i
;;;			lati     nums
;;;			jmp      code_0438
;;;code_0426:
;;;			lsl      numGames
;;;			ldi      20
;;;			eq?     
;;;			bnt      code_0436
;;;			lal      selected
;;;			lati     nums
;;;			jmp      code_0438
;;;code_0436:
;;;			lal      numGames
;;;code_0438:
;;;			sat      offset
;;;			jmp      code_0499
;;;			jmp      code_0287
;;;code_0440:
;;;			lsl      i
;;;			lofsa    okI
;;;			eq?     
;;;			bnt      code_0456
;;;			lal      selected
;;;			lati     nums
;;;			sat      offset
;;;			jmp      code_0499
;;;			jmp      code_0287
;;;code_0456:
;;;			lsl      i
;;;			ldi      0
;;;			eq?     
;;;			bt       code_0467
;;;			lsl      i
;;;			lofsa    cancelI
;;;			eq?     
;;;			bnt      code_0471
;;;code_0467:
;;;			ldi      65535
;;;			sat      offset
;;;			jmp      code_0499
;;;			jmp      code_0287
;;;code_0471:
;;;			lsl      theStatus
;;;			ldi      1
;;;			eq?     
;;;			bnt      code_0287
;;;			pushi    #cursor
;;;			pushi    1
;;;			pushi    1
;;;			pushi    2
;;;			lsp      theComment
;;;			lat      temp3
;;;			leai     @names
;;;			push    
;;;			callk    StrCpy,  4
;;;			push    
;;;			callk    StrLen,  2
;;;			push    
;;;			pushi    83
;;;			pushi    0
;;;			lofsa    editI
;;;			send     10
;;;			jmp      code_0287
;;;code_0499:
;;;			pushi    #dispose
;;;			pushi    0
;;;			self     4
;;;			lat      offset
;;;			ret     
;;;		)
;;;	)
;;;)
;;;

(class Restore of SRDialog
	(properties
		text {Restore a Game}
	)
)

(class Save of SRDialog
	(properties
		text {Save a Game}
	)
)

(instance GetReplaceName of Dialog
	(properties)
	
	(method (doit theComment &tmp ret)
      ; give ourself the system window as our window
      (= window SysWindow)
		(text1 setSize: moveTo: MARGIN MARGIN)
		(self add: text1 setSize:)
		(oldName
			text: theComment
			font: smallFont
			setSize:
			moveTo: MARGIN nsBottom
		)
		(self add: oldName setSize:)
		(text2 setSize: moveTo: MARGIN nsBottom)
		(self add: text2 setSize:)
		(newName
			text: theComment
			font: smallFont
			setSize:
			moveTo: MARGIN nsBottom
		)
		(self add: newName setSize:)
		(button1 nsLeft: 0 nsTop: 0 setSize:)
		(button2 nsLeft: 0 nsTop: 0 setSize:)
		(button2
			moveTo: (- nsRight (+ (button2 nsRight?) MARGIN)) nsBottom
		)
		(button1
			moveTo: (- (button2 nsLeft?) (+ (button1 nsRight?) MARGIN)) nsBottom
		)
		(self add: button1 button2 setSize: center: open: stdWindow 15)
		(= ret (super doit: newName))
		(self dispose:)
		(if (not (StrLen theComment))
			(NeedDescription)
			(= ret 0)
		)
		(return (if (== ret newName) else (== ret button1)))
	)
)

(instance selectorI of DSelector
	(properties
		x COMMENTSIZE
		y GAMESSHOWN
	)
)

(instance editI of DEdit
	(properties
		max (- COMMENTSIZE 1)
	)
)

(instance okI of DButton
	(properties)
)

(instance cancelI of DButton
	(properties
		text { Cancel_}
	)
)

(instance changeDirI of DButton
	(properties
		text {Change\0D\nDirectory}
	)
)

(instance textI of DText
	(properties
		font SYSFONT
	)
)

(instance text1 of DText
	(properties
		text {Replace}
		font SYSFONT
	)
)

(instance text2 of DText
	(properties
		text {with:}
		font SYSFONT
	)
)

(instance oldName of DText
	(properties)
)

(instance newName of DEdit
	(properties
		max (- COMMENTSIZE 1)
	)
)

(instance button1 of DButton
	(properties
		text {Replace}
	)
)

(instance button2 of DButton
	(properties
		text {Cancel}
	)
)

;EO: this is adapted from SCI16's SAVE.SC. It doesn't fully match the commented 
;assembly code below.
(procedure (GetDirectory where &tmp result [newDir 33] [buf1 40])
   (repeat
      (= result
         (Print SAVE 1
            #font SYSFONT
            #edit    (StrCpy @newDir where)
            #back
            #button {OK} TRUE
            #button {Cancel} FALSE
         )
      )
      ;Pressed ESC -- return FALSE.
      (if (not result)
         (return FALSE)
      )
      ;No string defaults to current drive.
      (if (not (StrLen @newDir))
         (GetCWD @newDir)
      )
      ;If drive is valid, return TRUE, otherwise complain.
      (if (ValidPath @newDir)
         (StrCpy where @newDir)
         (return TRUE)
      else
         (Print
         	(Format @buf1 SAVE 2 @newDir)
         	#font SYSFONT
         )		 
      )
   )
)
;EO: the disassembled output below.

;;;(procedure (GetDirectory where &tmp result [newDir 33] [buf 40])
;;;	(asm
;;;code_075a:
;;;		pushi    13
;;;		pushi    990
;;;		pushi    1
;;;		pushi    33
;;;		pushi    0
;;;		pushi    41
;;;		pushi    2
;;;		lea      @newDir
;;;		push    
;;;		lsp      where
;;;		callk    StrCpy,  4
;;;		push    
;;;		pushi    29
;;;		pushi    #button
;;;		lofsa    {OK}
;;;		push    
;;;		pushi    TRUE
;;;		pushi    #button
;;;		lofsa    {Cancel}
;;;		push    
;;;		pushi    FALSE
;;;		calle    Print,  26
;;;		sat      result
;;;		not     
;;;		bnt      code_078d
;;;		ldi      0
;;;		ret     
;;;code_078d:
;;;		pushi    1
;;;		lea      @newDir
;;;		push    
;;;		callk    StrLen,  2
;;;		not     
;;;		bnt      code_07a1
;;;		pushi    1
;;;		lea      @newDir
;;;		push    
;;;		callk    GetCWD,  2
;;;code_07a1:
;;;		pushi    1
;;;		lea      @newDir
;;;		push    
;;;		callk    ValidPath,  2
;;;		bnt      code_07bc
;;;		pushi    2
;;;		lsp      where
;;;		lea      @newDir
;;;		push    
;;;		callk    StrCpy,  4
;;;		ldi      1
;;;		ret     
;;;		jmp      code_075a
;;;code_07bc:
;;;		pushi    3
;;;		pushi    4
;;;		lea      @buf
;;;		push    
;;;		pushi    990
;;;		pushi    2
;;;		lea      @newDir
;;;		push    
;;;		callk    Format,  8
;;;		push    
;;;		pushi    33
;;;		pushi    0
;;;		calle    Print,  6
;;;		jmp      code_075a
;;;		ret     
;;;	)
;;;)


   (procedure (GetStatus)
      (return
         (cond
            ((== self Restore)
               RESTORE
            )
            ((HaveSpace)
               HAVESPACE
            )
            (numGames
               NOSPACE
            )
            (else
               NOREPLACE
            )
         )
	  )
  )

(procedure (HaveSpace)
	(if (< numGames MAXGAMES) (CheckFreeSpace curSaveDir))
)

(procedure (NeedDescription)
	(Print SAVE 3 #font 0)
)