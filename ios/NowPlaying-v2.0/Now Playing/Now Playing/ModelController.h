//
//  ModelController.h
//  Now Playing
//
//  Created by Kyle Buchanan on 3/31/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DataViewController;

@interface ModelController : NSObject <UIPageViewControllerDataSource>

- (DataViewController *)viewControllerAtIndex:(NSUInteger)index storyboard:(UIStoryboard *)storyboard;
- (NSUInteger)indexOfViewController:(DataViewController *)viewController;

@end
