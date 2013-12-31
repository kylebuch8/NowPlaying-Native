//
//  MainController.m
//  Now Playing
//
//  Created by Kyle Buchanan on 10/12/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import "MainController.h"
#import "MovieViewController.h"

@interface MainController ()

@end

@implementation MainController

@synthesize movieScrollView;

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
    // Do any additional setup after loading the view from its nib.
    
    self.movieScrollView.contentSize = CGSizeMake(CGRectGetWidth(self.movieScrollView.frame) * 2, CGRectGetHeight(self.movieScrollView.frame));
    
    MovieViewController *movieViewController = [[MovieViewController alloc] initWithNibName:@"MovieViewController" bundle:nil];
    [self.movieScrollView addSubview:movieViewController.view];
    
    MovieViewController *movieViewController2 = [[MovieViewController alloc] initWithNibName:@"MovieViewController" bundle:nil];
    CGRect frame = movieViewController2.view.frame;
    frame.origin.x = CGRectGetWidth(self.movieScrollView.frame);
    movieViewController2.view.frame = frame;
    [self.movieScrollView addSubview:movieViewController2.view];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
