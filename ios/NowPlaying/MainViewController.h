//
//  MainViewController.h
//  NowPlaying
//
//  Created by Kyle Buchanan on 12/30/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MovieContentViewController.h"
#import "GAITrackedViewController.h"

@interface MainViewController : GAITrackedViewController <UIPageViewControllerDataSource, UIPageViewControllerDelegate>

@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *loadingIndicator;
@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;

@property NSArray *movies;
@property (strong, nonatomic) UIPageViewController *pageViewController;

@end
