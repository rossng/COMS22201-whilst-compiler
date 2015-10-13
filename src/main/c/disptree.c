#include <ctype.h>
#include <stdio.h>
#include <string.h>

#define SIZ 100

int getword(char *word)
{
  int i = 0;
  char c = ' ';

  while (c != EOF && c <= ' ')
    c = getc(stdin);
  if (c == EOF)
    return 0;
  while (c != EOF && c > ' ') {
    if (i >= SIZ-1)
      return -1;
    i++;
    *word++ = c;
    if (c == '(' || c == ')') break;
    c = getc(stdin);
    if (c == '(' || c == ')') {
      ungetc(c, stdin);
      break;
    }
  }
  *word = '\0';
  return 1;
}

void err(char *mess)
{
  printf("%s\n", mess);
  exit(0);
}

indent(int n)
{
  int d;

  for (d=0; d<n; d++)
    putchar(' ');
}

void tree(int depth)
{
  int i = 0;
  char word[SIZ];

  i = getword(word);
  if (i < 1) err("no tree");
  if (strcmp(word, "(") == 0)
    err("badly formed tree");
  indent(depth);
  printf("%s\n", word);
  while (1) {
    i = getword(word);
    if (i < 1) break;
    if (strcmp(word, "(") == 0)
      tree(depth+1);
    else
    if (strcmp(word, ")") == 0)
      break;
    else {
      indent(depth+1);
      printf("%s\n", word);
    }
  }
  if (i < 0) err("token too long");
  if (i = 0) err("badly formed tree");
}

main(int argc, char *argv[])
{
  int i = 0;
  char word[SIZ];

  while (1) {
    i = getword(word);
    if (i < 0) err("token too long");
    if (i = 0) err("no tree");
    if (strcmp(word, "(") == 0) break;
  }
  tree(0);
}
