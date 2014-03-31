//
//  DataViewController.h
//  Now Playing
//
//  Created by Kyle Buchanan on 3/31/14.
//  Copyright (c) 2014 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DataViewController : UIViewController

@property (strong, nonatomic) IBOutlet UILabel *dataLabel;
@property (strong, nonatomic) id dataObject;

@end
