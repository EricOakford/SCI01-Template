;;; Sierra Script 1.0 - (do not remove this comment)
;**************************************************************
;***
;***	GAME.SH--
;***
;**************************************************************

; * Put all the defines specific to your game in here

(include system.sh) (include sci2.sh) ;system and kernel functions
(include pics.sh)   (include views.sh)  ;graphic defines

;Commonly-used header files are nested here, so most scripts only need to include this one.

; Script defines
(define MAIN 0)
(define TITLE 1)
(define TESTROOM 2)
(define SPEEDTEST 99)
(define DEBUG 800)

; Inventory items
;Make sure they are in the same order you put them in the inventory list in MAIN.SC.
;To avoid name conflicts, prefix the items with the letter "i".
(enum
	iTestObject
)

; Sound defines
(define sQuake 10)
(define sDeath 11)

;Event flags
	;Example: fBabaFrog (original Sierra naming)