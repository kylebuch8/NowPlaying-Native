//
//  RootViewController.h
//  Now Playing
//
//  Created by Kyle Buchanan on 3/31/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RootViewController : UIViewController <UIPageViewControllerDelegate>

@property (strong, nonatomic) UIPageViewController *pageViewController;

@end
