;;; Sierra Script 1.0 - (do not remove this comment)
; * SCI Game Header
;
; * Put all the defines specific to your game in here

; Script defines
(define MAIN 0)
(define TITLE 1)
(define TESTROOM 2)
(define SPEEDTEST 99)
(define DEBUG 800)

; Inventory items
;Make sure they are in the same order you put them in the inventory list in MAIN.SC. To avoid name conflicts, prefix the items with the letter "i".
(enum
	iTestObject
)

; View defines
(define vEgoWalk 0)
(define vEgoStand 1)
(define vSpeedTest 98)
(define vTestObject 800)
(define vEgoDeath 998)

; Pic defines
(define pTestRoom 2)
(define pSpeedTest 10)

; Sound defines
(define sQuake 10)
(define sDeath 11)
