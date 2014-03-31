//
//  MovieTrailerViewController.m
//  NowPlaying
//
//  Created by Kyle Buchanan on 1/24/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import "MovieTrailerViewController.h"

@interface MovieTrailerViewController ()

@end

@implementation MovieTrailerViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.automaticallyAdjustsScrollViewInsets = NO;
    
    [self.webView setOpaque:NO];
    
	NSString *htmlString = [NSString stringWithFormat:@"<html><head>\
    <meta name = \"viewport\" content = \"initial-scale = 1.0, user-scalable = no\"/></head>\
    <body style=\"background:#000;margin:0;\">\
    <div><iframe style=\"margin-top:184px;\" width=\"320\" height=\"200\" src=\"//www.youtube.com/embed/%@?rel=0\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>", self.youtubeId];
    
    [self.webView loadHTMLString:htmlString baseURL:[NSURL URLWithString:@"http://www.kristyandkyle.com"]];
    NSLog(@"Loaded iframe");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
