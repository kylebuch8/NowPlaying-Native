//
//  DataViewController.h
//  Now Playing
//
//  Created by Kyle Buchanan on 10/12/13.
//  Copyright (c) 2013 Kyle Buchanan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DataViewController : UIViewController

@property (strong, nonatomic) IBOutlet UILabel *dataLabel;
@property (strong, nonatomic) id dataObject;

@end
