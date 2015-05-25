# Solution-to-Android-Overscroll-Getting-Stuck

Came across a post on StackOverflow about an issue with Android Overscroll on devices running 4.x and greater. 

SO Post: http://stackoverflow.com/questions/30070523/overscrollby-doesnt-always-bounce-back-in-lollipop-5-x-platform

Overscroll Refrence: http://jasonfry.co.uk/blog/android-overscroll-revisited/


		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
			if (scrollY < 0) { // List being over scrolled from the top
				int scrollYMin = (scrollY * -1) - 1; // convert scrollY to a positive then subtract 1
				if (scrollYMin < 0) {// Due to subtracting 1 from scrollY, the base scrollY would be -1 rather than 0. So if scrollY is less than 0, we set scrollY to to its base value, 0.
					returnOverScrollBy = 0;
				} else {
					returnOverScrollBy = scrollYMin;
				}
			} else {  // List being over scrolled from the top
				int scrollYMin = scrollY - 1;
				if (scrollYMin < 0) {
					returnOverScrollBy = 0;
				} else {
					returnOverScrollBy = scrollYMin;
				}
			}

			if (isTouchEvent) {
				// Does exactly what it originally did on all previous versions.
				return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, true);
			} else {
				/**
				 * What I did:
				 * 1- Get the scrollY which is the current scroll value.  
				 * 2- Convert scrollY to positive if necessary. 
				 * 3- int returnOverScrollBy = scrollY - 1; 
				 * 4- returnOverScrollBy is always one less than scrollY current position.  
				 * finally- if isTouchEvent == false, we set the maxOverScrollY = returnOverScrollBy forcing maxOverScrollY to move programmatically, which prevents touch errors that were happening before. 
				 */
				return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, returnOverScrollBy, false);
			}
		}
