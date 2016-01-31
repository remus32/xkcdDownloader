# xkcd Downloader [![Build Status](https://travis-ci.org/remus32/xkcdDownloader.svg?branch=master)](https://travis-ci.org/remus32/xkcdDownloader) [![Coverage Status](https://coveralls.io/repos/github/remus32/xkcdDownloader/badge.svg?branch=master)](https://coveralls.io/github/remus32/xkcdDownloader?branch=master)
#### Download xkcd, search trough comic titles, show explanation and more
***
###### Install with:
```
git clone https://github.com/remus32/xkcdDownloader.git
cd xkcdDownloader
sbt run
```
***
###### Commands summary
1. Show
    *Usage: show comicId* - 
    Opens comic in new Swing window.
2. Quit
    *Usage: quit* - 
    Exit program.
3. Archive
    *Usage: archive comicId \[secondComicId\]* -
    Downloads comic images.
    If second comic id specified, downloads from from first to second id.
    
**Where comicId can be**
- **id** - Int comic id 
- **last** - Last published comic
- **random** - Random comic