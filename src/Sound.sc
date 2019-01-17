;;; Sierra Script 1.0 - (do not remove this comment)
(script# 989)
(include sci.sh)
(use Main)
(use System)


(class Sound of Object
	(properties
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
	
	(method (new param1)
		((super new:) owner: (if argc param1 else 0) yourself:)
	)
	
	(method (init)
		(= prevSignal (= signal 0))
		(sounds add: self)
		(DoSound sndSTOP self)
	)
	
	(method (dispose param1)
		(if (and argc (not param1)) (= client 0))
		(sounds delete: self)
		(if nodePtr (DoSound sndPAUSE self) (= nodePtr 0))
		(super dispose:)
	)
	
	(method (play param1)
		(self init:)
		(= client (if argc param1 else 0))
		(if (not loop) (= loop 1))
		(DoSound sndRESUME self 0)
	)
	
	(method (playBed param1)
		(self init:)
		(= client (if argc param1 else 0))
		(if (not loop) (= loop 1))
		(DoSound sndRESUME self 1)
	)
	
	(method (stop param1)
		(if (and argc (not param1)) (= client 0))
		(if nodePtr (DoSound sndVOLUME self))
	)
	
	(method (pause param1)
		(if (not argc) (= param1 1))
		(DoSound
			sndUPDATE
			(if (self isMemberOf: Sound) self else 0)
			param1
		)
	)
	
	(method (hold param1)
		(if (not argc) (= param1 1))
		(DoSound 14 self param1)
	)
	
	(method (release)
		(DoSound 14 self 0)
	)
	
	(method (fade param1 param2 param3 param4 param5)
		(if (and (> argc 4) param5) (= client 0))
		(if argc
			(DoSound sndFADE self param1 param2 param3 param4)
		else
			(DoSound sndFADE self 0 25 10 1)
		)
	)
	
	(method (send param1 param2 param3)
		(if (and (<= 1 param1) (<= param1 15))
			(DoSound sndSTOP_ALL self param1 param2 param3)
		)
	)
	
	(method (changeState)
		(DoSound sndSET_SOUND self)
	)
	
	(method (check)
		(DoSound sndCHECK_DRIVER self)
		(if signal
			(= prevSignal signal)
			(= signal 0)
			(if (IsObject client) (client cue: self))
		)
	)
	
	(method (clean param1)
		(if (or (not owner) (== owner param1))
			(self dispose:)
		)
	)
)
