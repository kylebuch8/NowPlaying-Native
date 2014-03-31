//
//  MovieTrailerViewController.h
//  NowPlaying
//
//  Created by Kyle Buchanan on 1/24/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MovieTrailerViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property NSString *youtubeId;

@end
