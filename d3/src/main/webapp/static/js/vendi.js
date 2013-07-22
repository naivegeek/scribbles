Diagram1 = function(){
  var svg = d3.select("#diagram-1").style("padding","30px");
  var postSeed;

  this.boot = function(){
      postSeed = new PostSeed().boot();
      drawPosts();
      drawTags();
  };

  var drawPosts = function(){
     //post circles
     svg.selectAll("circle").data(postSeed.allPosts()).enter().append("circle")
     .attr("cy",30).attr("cx",function(d,i){return i*40;})
     .attr("r",1).attr("fill","white")
     .transition().duration(1000).attr("r",function(d){return d.tagKeys.length*2})
     .attr("fill","grey").attr("stroke","black");


     //post labels
     svg.selectAll("text").data(postSeed.allPosts()).enter().append("text")
     .text(function(d){return d.name;})
     .attr("y",10).attr("x",function(d,i){return i*40;});
  };

  var drawTags = function(){
     //tag rectangles
     svg.selectAll("rect").data(postSeed.allTags()).enter().append("rect")
     .attr("y",160)
     .attr("x",function(d,i){return i*40;})
     .attr("width",30)
     .attr("height",1).transition().duration(1000).attr("height",function(d){return d.postKeys.length*3;})
     .attr("fill","lightblue").attr("stroke","black");

     //tag labels
     svg.selectAll("text").data(postSeed.allTags()).enter().append("text")
     .text(function(d){return d.name;})
     .attr("y",170).attr("x",function(d,i){return i*40;});
  };


};




//--------------- SEED  ---------------------------
Post = function(name){
   this.name = name;
   this.tags = {};
   this.tagKeys = [];

   this.addTag = function(tag){
     this.name = name;
     this.tagKeys.push(tag.name);
     this.tags[tag.name] = tag;
     tag.addPost(this);
   };
};
//-------------------------
Tag = function(name){
   this.name = name;
   this.posts = {};
   this.postKeys = [];

   this.addPost = function(post){
     this.postKeys.push(post.name);
     this.posts[post.name] = post;
   };

   this.hasPost = function(post){
     return posts[post.name] != null;
   };
};
//-------------------------
TagSeed = function(){
   var tags = new Array(8);

   this.random = function(){
     var randomSize = Math.floor((Math.random()*4)+3); //[3-7]tags/post
     var randomTags = new Array(randomSize);
     for(var i=0; i<randomSize; i++){
          var index = Math.floor(Math.random()*tags.length);
          randomTags[i] = tags[index];
     }
     return randomTags;
   };

   this.boot = function(){
     for(var i=0; i<tags.length; i++){
        tags[i] = new Tag("t_"+i);
     }
     return this;
   };

   this.allTags = function(){return tags};
};
//-------------------------
PostSeed = function(){
   var posts = new Array(25);
   var tagSeed = new TagSeed().boot();

   this.boot = function(){
     for(var i=0; i < posts.length; i++) {
        posts[i] = new Post("p_"+i);
        var tags = tagSeed.random();
        for(var j=0; j<tags.length; j++){
            posts[i].addTag(tags[j]);
        }
     }
     return this;
   };

   this.allPosts = function(){return posts};
   this.allTags = function(){return tagSeed.allTags()};
};
//-----------------------------------------------------
$(document).ready(function(){new Diagram1().boot()});