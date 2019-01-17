;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  QSOUND.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1993
;;;;
;;;;  Author:  Pablo Ghenis
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  This class of sound is meant to monitor sequentially-numbered cues
;;;;  and cue its client as many times as necessary to maintain the sequence,
;;;;  regardless of whether the machine is keeping up with the sound.
;;;;
;;;;  Classes:
;;;;     QueuedSound


(script# QSOUND)
(include system.sh) (include sci2.sh)
(use Sound)


(class QSound of Sound
	;;A QueuedSound assumes that the absolute values of the cues it receives
	;;form a sequence, ie. 128,129,130... 
	;;
	;;cue zero means "no news"
	;;ad-hoc cues are 1 to 127, must specialize "check" to handle them
	;;sequential cues go from 128 to 64k
	;;
	;;QueuedSounds eliminate the risk off having 
	;;the animation loop overrun by rapid sound cues since it catches up by 
	;;cueing its client as many times as the latest increment in signal, thus 
	;;faking a "queue of cues" (sorry, I can't resist a pun!)

	(properties
		name "QSnd"
		nodePtr 0
		handle 0
		number 0
		vol 127
		priority 0
		loop 1
		signal $0000
		prevSignal 0
		dataInc 0
		min 0
		sec 0
		frame 0
		client 0
		owner 0
	)
	
	(method (check &tmp cues theSignal [str 100])
		;; Used by the main game loop to determine if an animation
		;; cue or sound completion has occurred.  If so, cue:s the client.

		(DoSound UpdateCues self)
		(if (or (== signal -1) (u< signal 128))
			(return
				(if signal
					(= prevSignal signal)
					(= signal 0)
					(if (IsObject client) (client cue: self))
				else
					0
				)
			)
		)
		(while (!= (= theSignal signal) prevSignal)
			;;loop body
			;;---------
    
			(if (IsObject client)
				(= cues (- theSignal (if prevSignal else 127)))
				(while cues
				;;loop body
				;;---------

					(client cue: self)
					(-- cues)
				)
			)
			(= prevSignal theSignal)
		)
		(= signal 0)
		(return TRUE)
	)
)
