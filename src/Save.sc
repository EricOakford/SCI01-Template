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
	[butbuf2 4] = [{Select the game that you would like to restore.} {Type the description of this saved game.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.}]
)

(enum
   RESTORE        ;Restore games
   HAVESPACE      ;Save, with space on disk
   NOSPACE        ;Save, no space on disk but games to replace
   NOREPLACE      ;Save, no space on disk, no games to replace
)


(procedure (GetDirectory where &tmp result [newDir 33] [buf1 40])
   (repeat
      (= result
         (Print "New save-game directory:"
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
         	(Format @buf1 "%s\nis not a valid directory" @newDir)
         	#font SYSFONT
         )		 
      )
   )
)

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
	(Print "You must type a description for the game." #font 0)
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

         (= fd (FileIO fileOpen (Format @str "%ssg.dir" (theGame name?))))
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

            ((or (== i -1) (== i cancelI))
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
;      (DisposeScript FILE)
      (self dispose:)
;      (DisposeScript SAVE)
      (return ret)
   )
)

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
