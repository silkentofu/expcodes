#pragma once


// DialogProcessDetail �Ի���

class DialogProcessDetail : public CDialogEx
{
	DECLARE_DYNAMIC(DialogProcessDetail)

public:
	DialogProcessDetail(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~DialogProcessDetail();

// �Ի�������
	enum { IDD = IDD_PROC_DETAIL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
};