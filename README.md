Very simple code sample using ContentProvider and Loaders.

Tests are included.

* On application startup I check the **Last Modified** Header of the contentList, if it's not more recent than the one I have, then I don't bother; otherwise I assume that all data could be changed, so I get and save all of them.
* I use a ContentProvider.
* Items are saved as soon as I get the contentList (only if the contentList Last Modified date is more recent), then I start getting all the single items.
* I assume that we want to show only items that are in the current contentList, I disregard items that were downloaded and saved in the past.
* For Instrumentation Tests I use Robotium.

Maurizio Pietrantuono