;;; Sierra Script 1.0 - (do not remove this comment)
;**************************************************************
;***
;***	GAME.SH--
;***	Put all the defines specific to your game in here
;***
;**************************************************************

(include pics.sh) (include views.sh) ;graphical defines
(include system.sh) (include sci2.sh) ;system and kernel functions

; Commonly-used header files are nested here, so most scripts only need to include this one.

; howFast values
(enum
	slow
	medium
	fast
	fastest
)

; graphicsDriver values
(enum
	CGA	
	EGA
	VGA
)

;door states
(enum
	doorClosed
	doorOpening
	doorOpen
	doorClosing
)

; Game modules
(define MAIN			0)
(define SPEED			1)
(define DEBUG			2)
(define	INVDESC			3)	;inventory item descriptions (text-only)
(define GAME_INIT		4)
(define DISPOSE_CODE	5)
(define DOOR			6)

; Actual rooms
(define	TITLE		10)
(define	TESTROOM	11)

; Sound defines
(define sQuake 10)
(define sDeath 11)

; Inventory items
;Make sure they are in the same order you put them in the inventory list in MAIN.SC.
;To avoid name conflicts, prefix the items with the letter "i".
(enum
	iTestObject
	iLastInvItem	;this MUST be last
)

; Event flags
	;These flags are used by Bset, Btst, and Bclr.
	;Example: fBabaFrog (original Sierra naming)
(define FLAG_ARRAY 10)	;used for the gameFlags array. If you need more flags, increase this.
						;each global can have 16 flags. 10 globals * 16 flags = 160 flags.