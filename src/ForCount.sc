;;; Sierra Script 1.0 - (do not remove this comment)
(script# FORCOUNT)

;;; ForwardCounter Cycle Class
;;; Author J.Mark Hood 
;;; Saves states in scripts by cycleing a given number of times
;;; then cueing on completion.
;;; Usage : propName setCycle:ForwardCounter numOfCycles whoCares

(include system.sh) (include sci2.sh)
(use Motion)


(class ForwardCounter of Forward
	(properties
		client 0
		caller 0
		cycleDir 1
		cycleCnt 0
		completed 0
		count 0
	)
	
	(method (init theObj num whoCares)
		(super init: theObj)
		(if (>= argc 2)
			(= count num)
			(if (>= argc 3) (= caller whoCares)
			)
		)
	); init
	
	(method (cycleDone)
		(if (-- count)
			(super cycleDone:)
		else
			(= completed TRUE)
			(self motionCue:)
		)
   ); cycleDone

); ForwardCounter

