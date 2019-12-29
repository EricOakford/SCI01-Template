;;; Sierra Script 1.0 - (do not remove this comment)
;**************************************************************
;***
;***	GAME.SH--
;***	Put all the defines specific to your game in here
;***
;**************************************************************

; 

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

; Game modules
(enum
	MAIN			;0
	SPEEDTEST		;1
	DEBUG			;2
	INVDESC			;3	;inventory item descriptions (text-only)
	GAME_INIT		;4
	DISPOSE_CODE	;5
)

; Actual rooms
(enum 10
	TITLE		;10
	TESTROOM	;11
)

; Sound defines
(define sQuake 10)
(define sDeath 11)

; Inventory items
;Make sure they are in the same order you put them in the inventory list in MAIN.SC.
;To avoid name conflicts, prefix the items with the letter "i".
(enum
	iTestObject
)

; Event flags
	;These flags are used by Bset, Btst, and Bclr.
	;Example: fBabaFrog (original Sierra naming)